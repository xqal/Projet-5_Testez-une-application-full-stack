/// <reference types="cypress" />

describe('Auth and Account spec', () => {
    it('Register successfull, show account and delete account', () => {
        cy.visit('/register')

        // Enregistre l'utilisateur
        cy.get('input[formControlName=firstName]').type('new_user')
        cy.get('input[formControlName=lastName]').type('new_user')
        cy.get('input[formControlName=email]').type("newuser@newuser.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.get('button[type=submit]').click()

        cy.url().should('include', '/login')
        cy.get('input[formControlName=email]').type("newuser@newuser.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.get('button[type=submit]').click()
        cy.url().should('include', '/sessions')

        // Informations utilisateur
        cy.contains('span', 'Account').click();
        cy.url().should('include', '/me')
        cy.get('p').should('contain', 'Name: new_user NEW_USER')
        cy.get('p').should('contain', 'Email: newuser@newuser.com')

        // Supprimer le compte
        cy.contains('button', 'Detail').click();
        cy.get('.mat-snack-bar-container').should('contain', 'Your account has been deleted !');
        cy.url().should('include', '/')
    })

    it('Login failed wrong password', () => {
        cy.visit('/login')
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"password"}`)
        cy.get('button[type=submit]').click()
        
        cy.get('.error').should('be.visible');
        cy.url().should('include', '/login')
    })

    it('Login failed field empty', () => {
        cy.visit('/login')
        cy.get('button[type=submit]').should('be.disabled')

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('button[type=submit]').should('be.disabled')

        cy.get('input[formControlName=email]').clear();
        cy.get('input[formControlName=password]').type("password");
        cy.get('button[type=submit]').should('be.disabled');

        cy.url().should('include', '/login')
    })
});