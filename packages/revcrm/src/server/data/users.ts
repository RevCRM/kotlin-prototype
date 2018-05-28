import { ModelManager } from 'rev-models';
import { User } from '../../models/User/User';
import { UserAuthData } from '../../models/User/backend/UserAuthData';
import { Settings } from '../../models/Settings/Settings';

export async function populateUsers(models: ModelManager, settings: Settings) {

    await models.create(new User({
        email: 'admin@admin.com',
        active: true
    }));

    await models.create(new UserAuthData({
        userId: 1,
        username: 'admin',
        password: '$2a$10$.IJ0VzFPe.Ryi4gSrvhDWeH/9.VZ2sazAcgrtYjrMhVQ6001ItllW'
    }));

}
