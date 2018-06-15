
import { RevCRMClient } from 'revcrm/lib/client';
import { registerViews } from './views';
import { Account } from './models/Account';
import { Address } from './models/Address';

export function register(client: RevCRMClient) {
    client.models.register(Address);
    client.models.register(Account);
    registerViews(client.views);
}
