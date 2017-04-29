
import { ModelRegistry, InMemoryBackend } from 'rev-models';
import { User } from './User/User';

export const clientModels = new ModelRegistry();
clientModels.registerBackend('default', new InMemoryBackend());

clientModels.register(User);
