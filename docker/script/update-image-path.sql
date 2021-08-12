# Update muscle image path
UPDATE muscle_image mi
SET image_path = (SELECT CONCAT('image/', muscle_image_4.shortened_image_path)
                  FROM (SELECT muslce_image_3.id                         AS id,
                               SUBSTR(muslce_image_3.image_path, LENGTH(muslce_image_3.result_path) + 2,
                                      LENGTH(muslce_image_3.image_path)) AS shortened_image_path
                        FROM (SELECT id,
                                     image_path,
                                     SUBSTRING_INDEX((SELECT image_path
                                                      FROM muscle_image AS muscle_image_1
                                                      WHERE muscle_image_1.id = muscle_image_2.id), '/',
                                                     3) AS result_path
                              FROM muscle_image AS muscle_image_2) AS muslce_image_3) AS muscle_image_4
                  WHERE muscle_image_4.id = mi.id);

# Update exercise image path
UPDATE exercise e
SET exercise_gif_path = (SELECT CONCAT('image/', exercise_4.shortened_image_path)
                         FROM (SELECT exercise_3.id                                AS id,
                                      SUBSTR(exercise_3.exercise_gif_path, LENGTH(exercise_3.result_path) + 2,
                                             LENGTH(exercise_3.exercise_gif_path)) AS shortened_image_path
                               FROM (SELECT id,
                                            exercise_gif_path,
                                            SUBSTRING_INDEX((SELECT exercise_gif_path
                                                             FROM exercise AS exercise_1
                                                             WHERE exercise_1.id = exercise_2.id), '/',
                                                            2) AS result_path
                                     FROM exercise AS exercise_2) AS exercise_3) AS exercise_4
                         WHERE exercise_4.id = e.id);
