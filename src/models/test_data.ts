import { ModelManager } from 'rev-models';
import { User } from './User/User';
import { UserAuthData } from './User/backend/UserAuthData';
import { Perspective } from './UI/Perspective';
import { View } from './UI/View';

export async function populateTestData(models: ModelManager) {

    const accountListView = Object.assign(new View(), {
        name: 'list',
        label: 'All Accounts List',
        handler: 'ModelList'
    });

    const accountCreateView = Object.assign(new View(), {
        name: 'create',
        label: 'Create Account',
        handler: 'ModelForm'
    });

    return Promise.all([
        models.create(Object.assign(new User(), {
            email: 'admin@admin.com',
            active: true
        })),
        models.create(Object.assign(new UserAuthData(), {
            userId: 1,
            username: 'admin',
            password: '$2a$10$.IJ0VzFPe.Ryi4gSrvhDWeH/9.VZ2sazAcgrtYjrMhVQ6001ItllW'
        })),
        models.create(Object.assign(new Perspective(), {
            name: 'accounts',
            label: 'All Accounts',
            model: 'Account',
            views: [accountListView, accountCreateView]
        }))
    ]);

}
