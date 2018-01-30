
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api/lib/decorators';
import { BaseModel } from '../BaseModels';
import { PerspectiveView } from './PerspectiveView';

@ApiOperations(['read'])
export class Perspective extends BaseModel<Perspective> {

    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.TextField()
        title: string;
    @rev.TextField()
        model: string;
    @rev.RelatedModelList({ model: 'PerspectiveView', field: 'perspective' })
        views: PerspectiveView;

}
