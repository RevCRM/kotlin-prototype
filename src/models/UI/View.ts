
import * as rev from 'rev-models';
import { ApiOperations } from 'rev-api';
import { Perspective } from './Perspective';

@ApiOperations(['read'])
export class View {

    @rev.RecordField({ model: 'Perspective' })
        perspective: Perspective;
    @rev.TextField({ primaryKey: true })
        name: string;
    @rev.TextField()
        label: string;
    @rev.TextField()
        handler: string;

}
