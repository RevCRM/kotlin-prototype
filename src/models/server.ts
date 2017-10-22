
import { ModelManager, InMemoryBackend } from 'rev-models';
import { ModelApiManager } from 'rev-api';
import { User } from './User/User';
import { UserLogin } from './User/backend/UserLogin';
import { Company } from './Company/Company';
import { SelectionList } from './SelectionList/SelectionList';

export const serverModels = new ModelManager();
serverModels.registerBackend('default', new InMemoryBackend());
serverModels.register(User);
serverModels.register(UserLogin);
serverModels.register(Company);
serverModels.register(SelectionList);

export const api = new ModelApiManager(serverModels);
api.register(UserLogin, { model: 'UserLogin' });
api.register(Company);
api.register(SelectionList);
