
import { ModelRegistry, InMemoryBackend } from 'rev-models';
import { ModelApiRegistry } from 'rev-api';
import { User } from './User/User';
import { UserApi } from './User/api';

export const serverModels = new ModelRegistry();
serverModels.registerBackend('default', new InMemoryBackend());
serverModels.register(User);

export const api = new ModelApiRegistry(serverModels);
api.register(UserApi);
