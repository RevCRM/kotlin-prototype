
// register client models
import { Account } from './Accounts/Account';

export const clientModels = new ModelManager();
clientModels.registerBackend('default', new ModelApiBackend('/api'));

clientModels.register(UserLoginFormModel);
clientModels.register(Account);

// register server models