-- Seed: Primarchs (a sample of loyalists and traitors) + successor Chapters.
-- IDs are resolved by name to keep the migration idempotent.

INSERT INTO primarchs (name, legion_number, allegiance, status, homeworld, description)
VALUES
    ('Roboute Guilliman', 13, 'LOYALIST', 'ALIVE', 'Macragge', 'Primarch of the Ultramarines. Regent of Terra and author of the Codex Astartes.'),
    ('Lion El''Jonson', 1, 'LOYALIST', 'ALIVE', 'Caliban', 'Primarch of the Dark Angels. Master swordsman, returned in the Indomitus era.'),
    ('Leman Russ', 6, 'LOYALIST', 'MISSING', 'Fenris', 'Primarch of the Space Wolves. Lost within the Eye of Terror.'),
    ('Sanguinius', 9, 'LOYALIST', 'DECEASED', 'Baal', 'Primarch of the Blood Angels. Fell to Horus during the Siege of Terra.'),
    ('Rogal Dorn', 7, 'LOYALIST', 'MISSING', 'Inwit', 'Primarch of the Imperial Fists. Master of the defence of the Imperial Palace.'),
    ('Horus Lupercal', 16, 'TRAITOR', 'DECEASED', 'Cthonia', 'Warmaster. His heresy tore the galaxy apart.'),
    ('Magnus The Red', 15, 'TRAITOR', 'DAEMON_PRINCE', 'Próspero', 'Primarch of the Thousand Sons. Daemon Prince of Tzeentch.'),
    ('Angron', 12, 'TRAITOR', 'DAEMON_PRINCE', 'Nuceria', 'Primarch of the World Eaters. Daemon Prince of Khorne.')
ON CONFLICT (name) DO NOTHING;

INSERT INTO chapters (name, founding, primarch_id, homeworld, fortress_monastery, codex_compliant, approx_strength, description)
SELECT v.name, v.founding, p.id, v.homeworld, v.fortress, v.codex, v.strength, v.descr
FROM (VALUES
    ('Ultramarines', 1, 'Roboute Guilliman', 'Macragge', 'Fortress of Hera,', TRUE, 1000, 'XIII Legion. Exemplar of the Codex Astartes.'),
    ('Dark Angels', 1, 'Lion El''Jonson', 'Caliban ', 'The Rock', FALSE, 1000, 'I Legion. Secrets and the Hunt for the Fallen.'),
    ('Space Wolves', 1, 'Leman Russ', 'Fenris', 'The Fang', FALSE, 1000, 'VI Legion. Non-Codex compliant; organized into Great Companies.'),
    ('Blood Angels', 1, 'Sanguinius', 'Baal', 'Arx Angelicum', TRUE, 1000, 'IX Legion. Nobility marred by the Red Thirst.'),
    ('Imperial Fists', 1, 'Rogal Dorn', 'Terra (Phalanx)', 'Phalanx', TRUE, 1000, 'VII Legion. Experts in siege warfare and defence.'),
    ('Luna Wolves Legion', 1, 'Horus Lupercal', NULL, 'Vengeful Spirit', FALSE, 5000, 'Former Luna Wolves; core of Abaddon''s forces.'),
    ('Thousand Sons', 1, 'Magnus The Red', 'Sortiarius', 'Tower of the Cyclops', FALSE, 2000, 'XV Legion. Sorcerers in service to Tzeentch.'),
    ('World Eaters', 1, 'Angron', NULL, 'Conqueror', FALSE, 3000, 'XII Legion. Berserkers of Khorne.')
) AS v(name, founding, primarch_name, homeworld, fortress, codex, strength, descr)
JOIN primarchs p ON p.name = v.primarch_name
ON CONFLICT (name) DO NOTHING;
