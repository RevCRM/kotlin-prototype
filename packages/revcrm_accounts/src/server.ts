
import { RevCRMServer } from 'revcrm/lib/server';
import { Account } from './models/AccountBackend';

export async function register(server: RevCRMServer) {
    server.models.register(Account);
    server.api.register(Account);
}
