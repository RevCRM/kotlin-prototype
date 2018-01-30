import { ModelManager } from 'rev-models';
import { Account } from '../../models/Accounts/AccountBackend';

export async function populateAccounts(models: ModelManager) {

    await models.create(new Account({
        id: 1,
        type: 'organisation',
        name: 'Russoft Ltd',
        code: 'RUS001',
        website: 'http://www.russoft.com'
    }));

    await models.create(new Account({
        id: 2,
        type: 'organisation',
        name: 'Geeks R Us Co.',
        code: 'GEE001',
        website: 'http://www.geeks.com'
    }));

    await models.create(new Account({
        id: 3,
        type: 'organisation',
        name: 'Google',
        code: 'GOO001',
        website: 'http://www.google.com'
    }));

    await models.create(new Account({
        id: 4,
        type: 'organisation',
        name: 'AccuTech Gmbh',
        code: 'ACC001',
        website: 'http://www.accutech.com'
    }));

}
