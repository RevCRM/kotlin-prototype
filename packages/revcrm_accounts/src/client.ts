
import { RevCRMClient } from 'revcrm/lib/client';

// // register client models
// import { Account } from './Accounts/Account';

// export const clientModels = new ModelManager();
// clientModels.registerBackend('default', new ModelApiBackend('/api'));

// clientModels.register(UserLoginFormModel);
// clientModels.register(Account);

export function register(client: RevCRMClient) {
    console.log('revcrm_accounts client register');
}
