
import { ModelRegistry, InMemoryBackend } from 'rev-models';
import { UserLogin } from './User/UserLogin';

export const clientModels = new ModelRegistry();
clientModels.registerBackend('default', new InMemoryBackend());

clientModels.register(UserLogin);
