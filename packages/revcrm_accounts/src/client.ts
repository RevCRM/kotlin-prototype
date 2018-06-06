
import { RevCRMClient } from 'revcrm/lib/client';
import { registerViews } from './views';
import { Account } from './models/Account';

export function register(client: RevCRMClient) {
    client.models.register(Account);
    registerViews(client.views);
}
