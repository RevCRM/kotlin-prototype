
import { IApiDefinition } from 'rev-api';
import { User } from './User';

export const UserApi: IApiDefinition<User> = {
    model: User,
    operations: [ 'create', 'read', 'update', 'remove' ],
    methods: {
        login: {
            args: ['username', 'password'],
            handler: async (context, username, password) => {
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
            handler: async (context) => {
                return true;
            }
        }
    }
};
