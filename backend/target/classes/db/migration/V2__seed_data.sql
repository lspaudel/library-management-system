-- V2__seed_data.sql – initial admin user + sample data
-- Admin user: admin@library.com / Admin@123 (BCrypt hash)
INSERT INTO users (email, password, full_name, role, enabled, created_at, updated_at)
VALUES ('admin@library.com',
        '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTpyC5BxpO2',
        'Library Admin', 'ROLE_ADMIN', TRUE, NOW(), NOW());

-- Categories
INSERT INTO categories (name, description, created_at, updated_at) VALUES
    ('Fiction', 'Novels, short stories, and literary works', NOW(), NOW()),
    ('Science', 'Physics, Chemistry, Biology, Astronomy', NOW(), NOW()),
    ('Technology', 'Programming, AI, Databases, Cloud', NOW(), NOW()),
    ('History', 'World history, biographies, and chronicles', NOW(), NOW()),
    ('Philosophy', 'Ethics, metaphysics, logic, and political theory', NOW(), NOW()),
    ('Self-Help', 'Personal development and motivation', NOW(), NOW());

-- Authors
INSERT INTO authors (name, bio, created_at, updated_at) VALUES
    ('George Orwell', 'English novelist and essayist. Known for Nineteen Eighty-Four and Animal Farm.', NOW(), NOW()),
    ('J.K. Rowling', 'British author best known for the Harry Potter series.', NOW(), NOW()),
    ('Robert C. Martin', 'Software engineer and author of Clean Code.', NOW(), NOW()),
    ('Yuval Noah Harari', 'Israeli historian and author of Sapiens.', NOW(), NOW()),
    ('Frank Herbert', 'American author of the Dune science fiction series.', NOW(), NOW()),
    ('Martin Fowler', 'British software engineer known for Refactoring.', NOW(), NOW());

-- Books
INSERT INTO books (isbn, title, author_id, category_id, quantity, available_qty, published_year, description, created_at, updated_at) VALUES
    ('9780451524935', 'Nineteen Eighty-Four', 1, 1, 5, 5, 1949, 'Dystopian social science fiction novel.', NOW(), NOW()),
    ('9780439708180', 'Harry Potter and the Philosopher''s Stone', 2, 1, 8, 8, 1997, 'A young wizard discovers his destiny.', NOW(), NOW()),
    ('9780132350884', 'Clean Code', 3, 3, 4, 4, 2008, 'A handbook of agile software craftsmanship.', NOW(), NOW()),
    ('9780062316110', 'Sapiens: A Brief History of Humankind', 4, 4, 6, 6, 2011, 'History of our species from Stone Age to present.', NOW(), NOW()),
    ('9780441013593', 'Dune', 5, 1, 3, 3, 1965, 'Epic science fiction set in the far future.', NOW(), NOW()),
    ('9780201485677', 'Refactoring', 6, 3, 4, 4, 1999, 'Improving the Design of Existing Code.', NOW(), NOW());
