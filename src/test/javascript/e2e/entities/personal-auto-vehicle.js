'use strict';

describe('PersonalAutoVehicle e2e test', function () {

    var username = element(by.id('username'));
    var password = element(by.id('password'));
    var entityMenu = element(by.id('entity-menu'));
    var accountMenu = element(by.id('account-menu'));
    var login = element(by.id('login'));
    var logout = element(by.id('logout'));

    beforeAll(function () {
        browser.get('/');

        accountMenu.click();
        login.click();

        username.sendKeys('admin');
        password.sendKeys('admin');
        element(by.css('button[type=submit]')).click();
    });

    it('should load PersonalAutoVehicles', function () {
        entityMenu.click();
        element(by.css('[ui-sref="personal-auto-vehicle"]')).click().then(function() {
            expect(element.all(by.css('h2')).first().getText()).toMatch(/Personal Auto Vehicles/);
        });
    });

    it('should load create PersonalAutoVehicle dialog', function () {
        element(by.css('[ui-sref="personal-auto-vehicle.new"]')).click().then(function() {
            expect(element(by.css('h4.modal-title')).getText()).toMatch(/Create or edit a Personal Auto Vehicle/);
            element(by.css('button.close')).click();
        });
    });

    afterAll(function () {
        accountMenu.click();
        logout.click();
    });
});
