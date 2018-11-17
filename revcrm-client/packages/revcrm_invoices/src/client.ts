
import { RevCRMClient } from 'revcrm/lib/client';
import { registerViews } from './views/print';

export function register(client: RevCRMClient) {
    registerViews(client.views);
}
