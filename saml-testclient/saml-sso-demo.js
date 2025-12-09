const express = require('express');
const session = require('express-session');
const bodyParser = require('body-parser');
const passport = require('passport');
// Destructure SAML class so we can monkeypatch it safely at runtime
const passportSaml = require('passport-saml');
const SamlStrategy = passportSaml.Strategy || passportSaml;
const SAML = passportSaml.SAML;

// Monkeypatch: override SAML.prototype.validateSignature to always return true.
// This bypasses signature validation inside passport-saml and prevents
// "Invalid signature" errors. WARNING: this disables a critical security check
// and MUST NOT be used in production. Use only for local testing or when operating
// in a trusted environment.
if (SAML && SAML.prototype) {
    SAML.prototype._originalValidateSignature = SAML.prototype.validateSignature;
    SAML.prototype.validateSignature = function () {
        // Always indicate a valid signature.
        return true;
    };
}

const app = express();

app.use(bodyParser.urlencoded({extended: false}));
app.use(session({secret: 'your_secret', resave: false, saveUninitialized: true}));
app.use(passport.initialize());
app.use(passport.session());

// Configure SAML strategy
passport.use(new SamlStrategy(
    {
        entryPoint: 'http://localhost:8080/realms/idp/protocol/saml', // IdP SSO URL
        issuer: 'idbroker-saml',
        callbackUrl: 'http://localhost:3000/login/callback',
        cert: 'MIICfakecert', // Dummy certificate to bypass required check
        // Disable signature validation
        validateInResponseTo: false,
        disableRequestedAuthnContext: true,
        wantAssertionsSigned: false,
        wantAuthnResponseSigned: false,
        acceptedClockSkewMs: -1,
        // The following disables signature validation
        certSignatureAlgorithm: null,
        validateSignature: false,
        // Add this to ignore signature validation (passport-saml v3.x)
        validateXmlSignature: false,
        // Add this to ignore audience validation
        audience: null
    },
    function (profile, done) {
        // Save the SAML profile (token) in the session
        return done(null, profile);
    }
));

passport.serializeUser((user, done) => done(null, user));
passport.deserializeUser((user, done) => done(null, user));

// Routes
app.get('/', (req, res) => {
    if (req.isAuthenticated()) {
        res.send(`<h1>SAML Token</h1><pre>${JSON.stringify(req.user, null, 2)}</pre>`);
    } else {
        res.send('<a href="/login">Login with SAML</a>');
    }
});

app.get('/login',
    passport.authenticate('saml', {failureRedirect: '/', failureFlash: true})
);

app.post('/login/callback',
    passport.authenticate('saml', {failureRedirect: '/', failureFlash: true}),
    (req, res) => res.redirect('/')
);

app.listen(3000, () => {
    console.log('App listening on http://localhost:3000');
});
