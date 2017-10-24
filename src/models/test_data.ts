import { ModelManager } from 'rev-models';
import { User } from './User/User';
import { UserAuthData } from './User/backend/UserAuthData';

export async function populateTestData(models: ModelManager) {

    return Promise.all([
        models.create(Object.assign(new User(), {
            email: 'admin@admin.com',
            active: true
        })),
        models.create(Object.assign(new UserAuthData(), {
            userId: 1,
            username: 'admin',
            password: 'admin'
        }))
    ]);

}
