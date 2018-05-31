
import { populateUsers } from './users';
import { RevCRMServer } from '../server';

export async function populateData(server: RevCRMServer) {

    try {
        await populateUsers(server.models);
    }
    catch (e) {
        console.log('Error in populateData():', e);
        if (e.result && e.result.validation) {
            console.log('Validation Error:', e.result.validation);
        }
    }

}
