describe('sessions', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  })

  it('create session', () => {
      cy.intercept(
        {
          method: 'GET',
          url: '/api/teacher',
        },
        [
          {
            id: 1,
            lastName: 'DELAHAYE',
            firstName: 'Margot',
            createdAt: '2024-01-01 01:01:01',
            updatedAt: '2024-01-01 01:01:02',
          },
          {
            id: 2,
            lastName: 'THIERCELIN',
            firstName: 'Hélène',
            createdAt: '2024-02-02 02:02:02',
            updatedAt: '2024-02-02 02:02:02',
          },
        ]
      ).as('teacher');
  
      cy.intercept('POST', '/api/session', {
        body: [{
          id: 1,
          name: 'Nouvelle session Test',
          date: '2024-02-29',
          teacher_id: 1,
          description: 'Session de découverte',
          users: [],
          createdAt: '2024-02-29',
          updatedAt: '2024-02-29',
        }],
      });
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        [
          {
            id: 1,
            name: 'Nouvelle session Test',
            date: '2024-02-29',
            teacher_id: 1,
            description: 'Session de découverte',
            users: [],
            createdAt: '2024-02-29',
            updatedAt: '2024-02-29',
          },
        ]
      ).as('session');
  
      cy.get('button[routerLink="create"]').click();
  
      cy.url().should('include', '/sessions/create');

      cy.get('input[formControlName=name]').type("Nouvelle session Test");
      cy.get('input[formControlName=date]').type("2024-02-29");
      
      cy.get('mat-select').click();
      cy.get('mat-option').contains('Margot DELAHAYE').click();
      
      cy.get('textarea[formControlName=description]').type("Session de découverte");
      cy.get('button[type=submit]').click();

      cy.url().should('include', '/sessions');
  })

  it('delete session', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'Nouvelle session Test',
          date: '2024-02-29',
          teacher_id: 1,
          description: 'Session de découverte',
          users: [],
          createdAt: '2024-02-29',
          updatedAt: '2024-02-29',
        },
      ]
    ).as('session');

    cy.intercept(
      {
        method: 'GET', 
        url: '/api/session/1'
      }, 
        {
          id: 1,
          name: 'Nouvelle session Test',
          date: '2024-02-29',
          teacher_id: 1,
          description: 'Session de découverte',
          users: [],
          createdAt: '2024-02-29',
          updatedAt: '2024-02-29',
        }
    ).as('sessionInfo1');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      [
        {
          id: 1,
          lastName: 'DELAHAYE',
          firstName: 'Margot',
          createdAt: '2024-01-01 01:01:01',
          updatedAt: '2024-01-01 01:01:02',
        },
      ]
    ).as('teacher');

    cy.intercept('DELETE', '/api/session/1', {
      body: {
        id: 1,
        name: 'Nouvelle session Test',
        date: '2024-02-29',
        teacher_id: 1,
        description: 'Session de découverte',
        users: [],
        createdAt: '2024-02-29',
        updatedAt: '2024-02-29',
      },
    });

    cy.get('[ng-reflect-router-link="detail,1"]').click();

    cy.get(':nth-child(2) > .mat-focus-indicator').click();
  })

  it('update session', () => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      [
        {
          id: 1,
          name: 'Nouvelle session Test',
          date: '2024-02-29',
          teacher_id: 1,
          description: 'Session de découverte',
          users: [],
          createdAt: '2024-02-29',
          updatedAt: '2024-02-29',
        },
      ]
    ).as('session');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1'
      },
      {
        id: 1,
        name: 'Nouvelle session Test',
        date: '2024-02-29',
        teacher_id: 1,
        description: 'Session de découverte',
        users: [],
        createdAt: '2024-02-29',
        updatedAt: '2024-02-29',
      }
    ).as('sessionInfo1');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher/1',
      },
      [
        {
          id: 1,
          lastName: 'DELAHAYE',
          firstName: 'Margot',
          createdAt: '2024-01-01 01:01:01',
          updatedAt: '2024-01-01 01:01:02',
        },
        {
          id: 2,
          lastName: 'THIERCELIN',
          firstName: 'Hélène',
          createdAt: '2024-02-02 02:02:02',
          updatedAt: '2024-02-02 02:02:02',
        }
      ]
    ).as('teacher');

    cy.intercept('PUT', '/api/session/1', {
      body: {
        id: 1,
        name: 'Nouvelle session Modifié',
        date: '2024-03-29',
        teacher_id: 1,
        description: 'Session d apprentissage',
        users: [],
        createdAt: '2024-02-29',
        updatedAt: '2024-02-29',
      },
    });

      cy.get('.mat-card-actions > .ng-star-inserted').click();

      cy.url().should('include', '/sessions/update/1');

      cy.get('input[formControlName=name]').clear().type("Nouvelle session Modifié");
      cy.get('input[formControlName=date]').clear().type("2024-03-29");

      cy.get('mat-select').click();
      cy.get('mat-option').contains('Hélène THIERCELIN').click();

      cy.get('textarea[formControlName=description]').clear().type("Session d apprentissage");
      cy.get('button[type=submit]').click();

      
  })
})