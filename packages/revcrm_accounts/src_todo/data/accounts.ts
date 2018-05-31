import { ModelManager } from 'rev-models';
import { Account } from '../../models/Accounts/AccountBackend';

export async function populateAccounts(models: ModelManager) {

    await models.create(new Account({
        type: 'organisation',
        name: 'Russoft Ltd',
        code: 'RUS001',
        website: 'http://www.russoft.com'
    }));

    await models.create(new Account({
        type: 'organisation',
        name: 'Geeks R Us Co.',
        code: 'GEE001',
        website: 'http://www.geeks.com'
    }));

    await models.create(new Account({
        type: 'organisation',
        name: 'Google',
        code: 'GOO001',
        website: 'http://www.google.com'
    }));

    await models.create(new Account({
        type: 'organisation',
        name: 'AccuTech Gmbh',
        code: 'ACC001',
        website: 'http://www.accutech.com'
    }));

}
