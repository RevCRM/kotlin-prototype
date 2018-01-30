
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api/lib/decorators';
import { Perspective } from '../UI/Perspective';
import { EntityModel } from '../BaseModels';

@ApiOperations(['read'])
export class Settings extends EntityModel<Settings> {

    @rev.TextField()
        name: string;
    @rev.RelatedModel({ model: 'Perspective' })
        defaultPerspective: Perspective;

}
