import { ModelRegistry } from 'rev-models';
import { User } from './User/User';
import { ModelOperationResult } from 'rev-models/lib/operations/operationresult';

export function populateTestData(models: ModelRegistry) {

    return Promise.all([
        models.create(new User({
            username: 'admin',
            password: 'admin',
            email: 'admin@admin.com',
            active: true
        }))
    ])
    .catch((err) => {
        if (err.result) {
            const res: ModelOperationResult<any, any> = err.result;
            console.log(res.validation);
        }
    });

}
