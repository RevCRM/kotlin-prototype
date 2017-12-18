import { ModelManager } from 'rev-models';
import { populateUsers } from './users';
import { populateViews } from './views';
import { Settings } from '../../models/Settings/Settings';

export async function populateData(models: ModelManager) {

    const settings = new Settings();
    settings.name = 'default';

    try {
        await populateUsers(models, settings);
        await populateViews(models, settings);

        await models.create(settings);
    }
    catch (e) {
        console.log('Error in populateData():', e);
        if (e.result && e.result.validation) {
            console.log('Validation Error:', e.result.validation);
        }
    }

}
