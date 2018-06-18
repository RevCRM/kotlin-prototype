
import { RevCRMServer } from 'revcrm/lib/server';
import { Account } from './models/AccountBackend';
import { Address } from './models/Address';

export async function register(server: RevCRMServer) {
    server.models.register(Address);
    server.models.register(Account);
    server.api.register(Address);
    server.api.register(Account);
}
