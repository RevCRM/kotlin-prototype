
import { UIManager } from '../../UIManager';
import { Dashboard } from './components/Dashboard';

export function register(ui: UIManager) {
    ui.registerView({
        id: 'dashboard',
        model: null,
        component: Dashboard
    });
}
