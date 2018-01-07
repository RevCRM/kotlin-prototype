import { ModelManager } from 'rev-models';
import { Account } from '../../models/Accounts/AccountBackend';

export async function populateAccounts(models: ModelManager) {

    await models.create(new Account({
        id: 1,
        name: 'Russoft Ltd',
        code: 'RUS001',
        url: 'http://www.russoft.com'
    }));

    await models.create(new Account({
        id: 2,
        name: 'Geeks R Us Co.',
        code: 'GEE001',
        url: 'http://www.geeks.com'
    }));

    await models.create(new Account({
        id: 3,
        name: 'Google',
        code: 'GOO001',
        url: 'http://www.google.com'
    }));

    await models.create(new Account({
        id: 4,
        name: 'AccuTech Gmbh',
        code: 'ACC001',
        url: 'http://www.accutech.com'
    }));

}
