import { ModelManager } from 'rev-models';
import { populateUsers } from './users';
import { populateAccounts } from './accounts';

export async function populateData(models: ModelManager) {

    try {
        await populateUsers(models);
        await populateAccounts(models);
    }
    catch (e) {
        console.log('Error in populateData():', e);
        if (e.result && e.result.validation) {
            console.log('Validation Error:', e.result.validation);
        }
    }

}
