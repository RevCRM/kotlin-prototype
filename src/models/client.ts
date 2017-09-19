
import { ModelManager, InMemoryBackend } from 'rev-models';
import { UserLogin } from './User/UserLogin';

export const clientModels = new ModelManager();
clientModels.registerBackend('default', new InMemoryBackend());

clientModels.register(UserLogin);
