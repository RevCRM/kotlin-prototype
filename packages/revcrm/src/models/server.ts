
import { ModelManager, InMemoryBackend } from 'rev-models';
import { ModelApiManager } from 'rev-api';
import { User } from './User/User';
import { UserAuthData } from './User/backend/UserAuthData';
import { Account } from './Accounts/AccountBackend';
import { SelectionList } from './SelectionList/SelectionList';

const backend = new InMemoryBackend();
export const serverModels = new ModelManager();
serverModels.registerBackend('default', backend);
serverModels.register(UserAuthData);

export const api = new ModelApiManager(serverModels);

function registerCRNModels(models: any[]) {
    for (const model of models) {
        serverModels.register(model);
        api.register(model);
    }
}

registerCRNModels([
    User,
    Account,
    SelectionList,
]);
