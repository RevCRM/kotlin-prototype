
import { ModelManager, InMemoryBackend } from 'rev-models';
import { ModelApiManager } from 'rev-api';
import { User } from './User/User';
import { UserAuthData } from './User/backend/UserAuthData';
import { UserLoginFormModel } from './User/backend/UserLoginFormModel';
import { Company } from './Company/Company';
import { SelectionList } from './SelectionList/SelectionList';

export const serverModels = new ModelManager();
serverModels.registerBackend('default', new InMemoryBackend());
serverModels.register(User);
serverModels.register(UserAuthData);
serverModels.register(UserLoginFormModel);
serverModels.register(Company);
serverModels.register(SelectionList);

export const api = new ModelApiManager(serverModels);
api.register(UserLoginFormModel);
api.register(Company);
api.register(SelectionList);
