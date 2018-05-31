
import { ModelManager } from 'rev-models';
import { ModelApiBackend } from 'rev-api-client';
import { UserLoginFormModel } from './User/forms/UserLoginForm';

export const clientModels = new ModelManager();
clientModels.registerBackend('default', new ModelApiBackend('/api'));

clientModels.register(UserLoginFormModel);
