

INSERT INTO primarchs (name, legion_number, allegiance, status, homeworld, description)
VALUES
    ('Fulgrim', 3, 'TRAITOR', 'DAEMON_PRINCE', 'Chemos', 'Primarch of the Emperor''s Children. Ascended as a Daemon Prince of Slaanesh.'),
    ('Perturabo', 4, 'TRAITOR', 'DAEMON_PRINCE', 'Olympia', 'Primarch of the Iron Warriors. Master of siege warfare, now a Daemon Prince.'),
    ('Jaghatai Khan', 5, 'LOYALIST', 'MISSING', 'Chogoris', 'Primarch of the White Scars. Lost in the Webway after chasing Dark Eldar raiders.'),
    ('Konrad Curze', 8, 'TRAITOR', 'DECEASED', 'Nostramo', 'Primarch of the Night Lords. The Night Haunter, allowed his own assassination.'),
    ('Ferrus Manus', 10, 'LOYALIST', 'DECEASED', 'Medusa', 'Primarch of the Iron Hands. Beheaded by Fulgrim at Istvaan V.'),
    ('Mortarion', 14, 'TRAITOR', 'DAEMON_PRINCE', 'Barbarus', 'Primarch of the Death Guard. Daemon Prince of Nurgle.'),
    ('Lorgar Aurelian', 17, 'TRAITOR', 'DAEMON_PRINCE', 'Colchis', 'Primarch of the Word Bearers. Author of the Lectitio Divinitatus, first to fall to Chaos.'),
    ('Vulkan', 18, 'LOYALIST', 'MISSING', 'Nocturne', 'Primarch of the Salamanders. Perpetual, keeper of humanity''s flame.'),
    ('Corvus Corax', 19, 'LOYALIST', 'MISSING', 'Deliverance', 'Primarch of the Raven Guard. Vanished into the Eye of Terror seeking penance.'),
    ('Alpharius Omegon', 20, 'TRAITOR', 'UNKNOWN', 'Unknown', 'Twin Primarchs of the Alpha Legion. Masters of deception; fate unknown.')
ON CONFLICT (name) DO NOTHING;

INSERT INTO chapters (name, founding, primarch_id, homeworld, fortress_monastery, codex_compliant, approx_strength, description)
SELECT v.name, v.founding, p.id, v.homeworld, v.fortress, v.codex, v.strength, v.descr
FROM (VALUES
    ('Emperor''s Children', 1, 'Fulgrim', 'Chemos', 'Palace of the Legion', FALSE, 2000, 'III Legion. Perfectionists fallen to Slaanesh.'),
    ('Iron Warriors', 1, 'Perturabo', 'Olympia', 'Medrengard', FALSE, 3000, 'IV Legion. Siege masters turned to Chaos Undivided.'),
    ('White Scars', 1, 'Jaghatai Khan', 'Chogoris', 'Quan Zhou', TRUE, 1000, 'V Legion. Fast attack specialists of the steppes.'),
    ('Night Lords', 1, 'Konrad Curze', 'Nostramo', 'Nightfall', FALSE, 2500, 'VIII Legion. Terror tactics and midnight clad.'),
    ('Iron Hands', 1, 'Ferrus Manus', 'Medusa', 'Medusa Forgeholds', TRUE, 1000, 'X Legion. Flesh is weak; the machine endures.'),
    ('Death Guard', 1, 'Mortarion', 'Barbarus', 'Plague Planet', FALSE, 3000, 'XIV Legion. Plague Marines of Nurgle.'),
    ('Word Bearers', 1, 'Lorgar Aurelian', 'Colchis', 'Sicarus', FALSE, 4000, 'XVII Legion. Dark Apostles of Chaos.'),
    ('Salamanders', 1, 'Vulkan', 'Nocturne', 'Prometheus', TRUE, 1000, 'XVIII Legion. Forgemasters and protectors of civilians.'),
    ('Raven Guard', 1, 'Corvus Corax', 'Deliverance', 'Ravenspire', TRUE, 1000, 'XIX Legion. Masters of stealth and guerrilla war.'),
    ('Alpha Legion', 1, 'Alpharius Omegon', NULL, 'Alpharius (flagship)', FALSE, 3000, 'XX Legion. Hydra Dominatus; loyalty unknown.')
) AS v(name, founding, primarch_name, homeworld, fortress, codex, strength, descr)
JOIN primarchs p ON p.name = v.primarch_name
ON CONFLICT (name) DO NOTHING;
