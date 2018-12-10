
import { UIManager } from '../../UIManager';
import { Dashboard } from './components/Dashboard';

export function register(ui: UIManager) {

    ui.registerPerspective({
        id: 'dashboard',
        title: 'Dashboard',
        views: {
            my: {
                title: 'My Dashboard',
                viewId: 'my_dashboard',
            },
        }
    });

    ui.registerView({
        id: 'my_dashboard',
        model: null,
        component: Dashboard
    });
}
