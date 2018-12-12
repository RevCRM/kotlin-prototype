
import { UIManager } from '../../UIManager';

export function register(ui: UIManager) {

    ui.registerMenu({
        id: 'menu_calendar',
        label: 'Calendar',
        perspective: 'calendar',
        icon: 'insert_invitation',
        subItems: [
            { label: 'My Calendar', perspective: 'calendar', view: 'my' },
            { label: 'Team Calendar', perspective: 'calendar', view: 'team' },
        ]
    });
    ui.registerMenu({
        id: 'menu_opportunities',
        label: 'Sales & Marketing',
        perspective: 'opportunities',
        icon: 'monetization_on_sharp',
        subItems: [
            { label: 'Sales Opportunities', perspective: 'opportunities', view: 'list' },
        ]
    });
    ui.registerMenu({
        id: 'menu_cases',
        label: 'Customer Service',
        perspective: 'cases',
        icon: 'assignment',
        subItems: [
            { label: 'My Cases', perspective: 'cases', view: 'my' },
            { label: 'Team Cases', perspective: 'cases', view: 'team' },
        ]
    });

}
