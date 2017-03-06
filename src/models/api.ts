
import * as api from 'rev-api';

import User from './User/User';

api.register(User, {
    operations: 'all',
    methods: [
        {
            name: 'login',
            args: ['username', 'password'],
            handler: (context, username, password) => {
                if (username == 'admin' && password == 'admin') {
                    return true;
                }
                else {
                    return false;
                }
            }
        },
        {
            name: 'logout',
            args: [],
            handler: (context) => {
                return true;
            }
        }
    ]
});
