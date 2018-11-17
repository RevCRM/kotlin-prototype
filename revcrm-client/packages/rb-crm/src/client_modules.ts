// Auto-generated for webpack build
import { RevCRMClient } from 'revcrm/lib/client';
import { register as register_revcrm_accounts } from 'revcrm_accounts/lib/client';
import { register as register_revcrm_invoices } from 'revcrm_invoices/lib/client';

export async function registerModules(client: RevCRMClient) {
    await register_revcrm_accounts(client);
    await register_revcrm_invoices(client);
}
