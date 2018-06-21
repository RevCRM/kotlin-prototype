
import { RevCRMClient } from 'revcrm/lib/client';
import { registerViews } from './views';
import { Account } from './models/Account';
import { Address } from './models/Address';
import { AccountLink } from './models/AccountLink';

export function register(client: RevCRMClient) {
    client.models.register(Address);
    client.models.register(Account);
    client.models.register(AccountLink);
    registerViews(client.views);
}
