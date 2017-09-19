
import { ModelManager, InMemoryBackend } from 'rev-models';
import { ModelApiManager } from 'rev-api';
import { User } from './User/User';
import { UserApi } from './User/api/api';
import { UserLoginBackend } from './User/api/UserLoginBackend';

export const serverModels = new ModelManager();
serverModels.registerBackend('default', new InMemoryBackend());
serverModels.register(User);
serverModels.register(UserLoginBackend);

export const api = new ModelApiManager(serverModels);
api.register(UserApi);
