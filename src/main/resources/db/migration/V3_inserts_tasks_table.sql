INSERT INTO tasks (id, title, description, status, priority, created_at, updated_at, due_date)
VALUES
  ( gen_random_uuid(), 'Comprar mantimentos', 'Comprar pão, leite e ovos', 'pending', 'medium', NOW(), NOW(), NOW() + INTERVAL '2 days'),
  ( gen_random_uuid(), 'Finalizar relatório', 'Concluir relatório mensal para o chefe', 'in_progress', 'high', NOW(), NOW(), NOW() + INTERVAL '1 day'),
  ( gen_random_uuid(), 'Limpar escritório', 'Organizar papéis e mesa', 'done', 'low', NOW(), NOW(), NOW() + INTERVAL '5 days'),
  ( gen_random_uuid(), 'Estudar Spring Boot', 'Praticar endpoints e JPA', 'pending', 'high', NOW(), NOW(), NOW() + INTERVAL '3 days'),
  ( gen_random_uuid(), 'Treinar React', 'Criar componentes e hooks', 'in_progress', 'medium', NOW(), NOW(), NOW() + INTERVAL '7 days'),
  ( gen_random_uuid(), 'Reunião com time', 'Planejamento da sprint', 'pending', 'high', NOW(), NOW(), NOW() + INTERVAL '1 day'),
  ( gen_random_uuid(), 'Enviar proposta', 'Proposta de projeto para cliente', 'done', 'medium', NOW(), NOW(), NOW() + INTERVAL '4 days'),
  ( gen_random_uuid(), 'Revisar código', 'Revisar PRs abertos no GitHub', 'in_progress', 'high', NOW(), NOW(), NOW() + INTERVAL '2 days'),
  ( gen_random_uuid(), 'Agendar consultas', 'Marcar consultas médicas', 'pending', 'low', NOW(), NOW(), NOW() + INTERVAL '10 days'),
  ( gen_random_uuid(), 'Fazer backup', 'Backup do banco de dados', 'done', 'medium', NOW(), NOW(), NOW() + INTERVAL '1 day');