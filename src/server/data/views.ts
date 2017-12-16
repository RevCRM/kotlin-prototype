import { ModelManager } from 'rev-models';
import { View } from '../../models/UI/View';
import { Perspective } from '../../models/UI/Perspective';
import { PerspectiveView } from '../../models/UI/PerspectiveView';

export async function populateViews(models: ModelManager) {

    const perspective = (await models.create(new Perspective({
        name: 'accounts',
        label: 'All Accounts',
        model: 'Account'
    }))).result;

    const listView = (await models.create(new View ({
        name: 'account_list',
        label: 'Accounts List',
        handler: 'ModelList'
    }))).result;

    await models.create(new PerspectiveView({
        perspective: perspective,
        view: listView
    }));

    const formView = (await models.create(new View({
        name: 'account_form',
        label: 'Account',
        handler: 'ModelForm'
    }))).result;

    await models.create(new PerspectiveView({
        perspective: perspective,
        view: formView
    }));

}
