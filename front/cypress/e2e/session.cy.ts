/// <reference types="cypress" />

describe('Session spec admin', () => {
  beforeEach(() => {
    cy.visit('/sessions')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  //Lister les sessions
  it('should list all sessions', () => {
    cy.get('.item').should('be.visible')
  })

  //afficher les bouton create and edit si admin
  it('should display create button', () => {
    cy.contains('button', 'Create').should('be.visible');
    cy.get('.item').first().within(() => {
      cy.contains('button', 'Detail').should('be.visible');
      cy.contains('button', 'Edit').should('be.visible');
    });
  });

  // Creation, information et suppression d'une session
  it('should create, display information and delete a session', () => {
    const sessionName = `E2e Session Name`;

    // Création de la session
    cy.contains('button', 'Create').click();
    cy.url().should('include', '/sessions/create');

    cy.get('input[formControlName=name]').type(sessionName);
    cy.get('input[formControlName=date]').type("2026-01-24");
    
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').first().click();

    cy.get('textarea[formControlName=description]').type("Description test e2e session créer");
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions');
    cy.contains(sessionName).should('be.visible');

    //Information de session
    cy.contains('.item', sessionName).within(() => {
      cy.contains('Detail').click();
    });
    cy.url().should('include', '/detail');

    cy.get('h1').should('contain', sessionName);
    cy.contains('Description test e2e session créer').should('be.visible');
    cy.contains('January 24, 2026').should('be.visible');



    // Supprimer la session
    cy.contains('button', 'Delete').click();
    cy.url().should('include', '/sessions');
    cy.contains(sessionName).should('not.exist');
    cy.get('.mat-snack-bar-container').should('contain', 'Session deleted !');
  });

});

describe('Session spec user', () => {
  beforeEach(() => {
    cy.visit('/sessions')
    cy.get('input[formControlName=email]').type("user@user.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('should add participant to the session', () => {
    cy.get('.item').first().within(() => {
      cy.contains('button', 'Detail').click();
    });
    cy.url().should('include', '/detail');
    cy.contains('button', 'Participate').should('be.visible');
    cy.contains('button', 'Delete').should('not.exist');

    // Ajouter le participant
    cy.contains('button', 'Participate').click();
    cy.contains('button', 'Do not participate').should('be.visible');

    // Supprimer le participant
    cy.contains('button', 'Do not participate').click();
    cy.contains('button', 'Participate').should('be.visible');

  });
});