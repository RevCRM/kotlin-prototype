import { ModelRegistry } from 'rev-models';
import { User } from './User/BackendUser';

export function populateTestData(models: ModelRegistry) {

    return Promise.all([
        models.create(new User({
            username: 'admin',
            password: 'admin',
            email: 'admin@admin.com',
            active: true,
            secret_field: 'shhhhh'
        }))
    ]);

}
