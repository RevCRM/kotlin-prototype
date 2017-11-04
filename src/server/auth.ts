
import * as Koa from 'koa';
import * as passport from 'koa-passport';
import { Strategy as LocalStrategy } from 'passport-local';
import * as bcrypt from 'bcrypt';

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

passport.use(new LocalStrategy(async (username, password, done) => {
    try {
        const match = await serverModels.read(UserAuthData, {
            username: username
        });
        if (match.results.length == 0) {
            done(null, false);
        }
        else {
            const user = match.results[0];
            const pwMatch = await bcrypt.compare(password, user.password);
            if (pwMatch) {
                done(null, match.results[0]);
            }
            else {
                done(null, false);
            }
        }
    }
    catch (err) {
        done(err, null);
    }
}));

export interface IAuthOptions {
    unauthenticatedUrls: string [];
    loginUrl: string;
}

export function requireAuth(options: IAuthOptions) {
    return async (ctx: Koa.Context, next: () => Promise<any>) => {
        if (options.unauthenticatedUrls.indexOf(ctx.path) > -1 || ctx.isAuthenticated()) {
            return next();
        }
        else {
            ctx.redirect(options.loginUrl);
        }
    };
}
