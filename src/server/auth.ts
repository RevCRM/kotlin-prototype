
import * as passport from 'koa-passport';
import { Strategy as LocalStrategy } from 'passport-local';

import { serverModels } from '../models/server';
import { UserAuthData } from '../models/User/backend/UserAuthData';

passport.serializeUser((user: UserAuthData, done) => {
    done(null, user.id);
});

passport.deserializeUser(async (id, done) => {
    try {
        const res = await serverModels.read(UserAuthData, { id: id });
        done(null, res.results[0]);
    }
    catch (err) {
        done(err, null);
    }
});

// TODO: HASH + SALT PASSWORDS !!!!!!!
passport.use(new LocalStrategy(async (username, password, done) => {
    try {
        const match = await serverModels.read(UserAuthData, {
            username: username,
            password: password
        });
        done(null, match.results[0]);
    }
    catch (err) {
        done(err, null);
    }
}));
