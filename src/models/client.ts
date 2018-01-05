
import { ModelManager } from 'rev-models';
import { ModelApiBackend } from 'rev-api-client';
import { UserLoginFormModel } from './User/forms/UserLoginForm';
import { Settings } from './Settings/Settings';

export const clientModels = new ModelManager();
clientModels.registerBackend('default', new ModelApiBackend('/api'));

clientModels.register(UserLoginFormModel);
clientModels.register(Settings);
