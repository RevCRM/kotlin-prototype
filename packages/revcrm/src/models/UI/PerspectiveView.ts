
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api/lib/decorators';
import { View } from './View';
import { Perspective } from './Perspective';
import { BaseModel } from '../BaseModels';

@ApiOperations(['read'])
export class PerspectiveView extends BaseModel<PerspectiveView> {

    @rev.AutoNumberField({ primaryKey: true })
        id: number;
    @rev.RelatedModel({ model: 'Perspective' })
        perspective: Perspective;
    @rev.RelatedModel({ model: 'View' })
        view: View;
    @rev.BooleanField({ required: false })
        isDefault: boolean;

}
