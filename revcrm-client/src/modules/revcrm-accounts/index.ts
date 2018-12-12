
import { UIManager } from '../../UIManager';
import { AccountList } from './components/AccountList';

export function register(ui: UIManager) {

    ui.registerPerspective({
        id: 'accounts',
        title: 'Accounts',
        views: {
            companies: {
                title: 'Companies',
                viewId: 'accounts_companies',
            },
        }
    });

    ui.registerView({
        id: 'accounts_companies',
        model: 'account',
        component: AccountList
    });

    ui.registerMenu({
        id: 'menu_accounts',
        label: 'Companies & Contacts',
        perspective: 'accounts',
        icon: 'supervised_user_circle',
        subItems: [
            { label: 'Companies', perspective: 'accounts', view: 'companies' },
            { label: 'Contacts', perspective: 'accounts', view: 'contacts' },
            { label: 'Leads', perspective: 'accounts', view: 'leads' },
            { label: 'Data Import', perspective: 'accounts', view: 'import' },
        ]
    });

}
