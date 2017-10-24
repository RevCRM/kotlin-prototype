import { ModelManager } from 'rev-models';
import { User } from './User/User';

export async function populateTestData(models: ModelManager) {

    return Promise.all([
        models.create(Object.assign(new User(), {
            username: 'admin',
            email: 'admin@admin.com',
            active: true
        }))
    ]);

}
