
import * as api from 'rev-api';

import User from './User/User';

api.register(User, {
    operations: 'all',
    methods: {
        login: {
            args: ['username', 'password'],
            handler: async(context, username, password) => {
                if (username == 'admin' && password == 'admin') {
                    return true;
                }
                else {
                    return false;
                }
            }
        },
        logout: {
            args: [],
            handler: async(context) => {
                return true;
            }
        }
    }
});
