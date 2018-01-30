
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api/lib/decorators';
import { BaseModel } from '../BaseModels';
import { PerspectiveView } from './PerspectiveView';

@ApiOperations(['read'])
export class View extends BaseModel<View> {

    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.TextField()
        label: string;
    @rev.TextField()
        handler: string;
    @rev.RelatedModelList({ model: 'PerspectiveView', field: 'view' })
        perspectives: PerspectiveView;

}
